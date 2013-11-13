package org.kuali.rice.devtools.jpa.eclipselink.conv.parser.helper.resolver;

import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.CollectionDescriptor;
import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.ojb.broker.metadata.ObjectReferenceDescriptor;
import org.kuali.rice.devtools.jpa.eclipselink.conv.ojb.OjbUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * this only does mappings for OneToOne at the field level for compound fks.
 */
public abstract class AbstractJoinColumnResolver extends AbstractMappedFieldResolver {
    private static final Log LOG = LogFactory.getLog(AbstractJoinColumnResolver.class);

    public AbstractJoinColumnResolver(Collection<DescriptorRepository> descriptorRepositories) {
        super(descriptorRepositories);
    }

    protected final List<Expression> getJoinColumns(String enclosingClass, String fieldName, String mappedClass) {
        final ObjectReferenceDescriptor ord = OjbUtil.findObjectReferenceDescriptor(mappedClass, fieldName,
                descriptorRepositories);

        final CollectionDescriptor cld = OjbUtil.findCollectionDescriptor(mappedClass, fieldName,
                descriptorRepositories);

        final List<Expression> joinColumns = new ArrayList<Expression>();

        if (foundDescriptor(ord, cld) && !isMToN(cld)) {

            final Collection<String> fks = getForeignKeys(ord, cld);
            if (fks == null || fks.isEmpty()) {
                LOG.error(ResolverUtil.logMsgForField(enclosingClass, fieldName, mappedClass) + " field has a reference descriptor for " + fieldName
                        + " but does not have any foreign keys configured");
                return null;
            }

            final Collection<String> pks = OjbUtil.getPrimaryKeyNames(mappedClass, descriptorRepositories);

            if (pks.size() == fks.size() && !pks.containsAll(fks) && !pks.isEmpty()) {

                final ClassDescriptor cd = OjbUtil.findClassDescriptor(mappedClass, descriptorRepositories);
                final ClassDescriptor icd;

                final String itemClassName = getItemClass(ord, cld);
                if (StringUtils.isBlank(itemClassName)) {
                    LOG.error(ResolverUtil.logMsgForField(enclosingClass, fieldName, mappedClass) + " field has a reference descriptor for " + fieldName
                            + " but does not class name attribute");
                    return null;
                } else {
                    icd = OjbUtil.findClassDescriptor(itemClassName, descriptorRepositories);
                }

                final FieldDescriptor[] pfds = getForeignKeysDescr(cd, ord, cld);
                final FieldDescriptor[] ipfds = icd.getPkFields();
                for (int i = 0; i < pfds.length; i++) {
                    final List<MemberValuePair> pairs = new ArrayList<MemberValuePair>();
                    if (!isAnonymousFk(pfds[i])) {
                        pairs.add(new MemberValuePair("insertable", new BooleanLiteralExpr(false)));
                        pairs.add(new MemberValuePair("updatable", new BooleanLiteralExpr(false)));
                    }

                    if (!isNullableFk(pfds[i])) {
                        pairs.add(new MemberValuePair("nullable", new BooleanLiteralExpr(false)));
                    }

                    pairs.add(new MemberValuePair("name", new StringLiteralExpr(pfds[i].getColumnName())));
                    pairs.add(new MemberValuePair("referencedColumnName", new StringLiteralExpr(ipfds[i].getColumnName())));
                    joinColumns.add(new NormalAnnotationExpr(new NameExpr("JoinColumn"), pairs));
                }
            }
        }
        return joinColumns;
    }

    private boolean foundDescriptor(ObjectReferenceDescriptor ord, CollectionDescriptor cld) {
        return ord != null || cld != null;
    }

    private List<String> getForeignKeys(ObjectReferenceDescriptor ord, CollectionDescriptor cld) {
        if (ord != null) {
            return ord.getForeignKeyFields();
        } else if (cld != null) {
            cld.getForeignKeyFields();
        }
        return null;
    }

    private FieldDescriptor[] getForeignKeysDescr(ClassDescriptor cd, ObjectReferenceDescriptor ord, CollectionDescriptor cld) {
        final List<String> fks = getForeignKeys(ord, cld);
        if (fks != null) {
            final List<FieldDescriptor> pfds = new ArrayList<FieldDescriptor>();
            for (String fk : fks) {
                pfds.add(cd.getFieldDescriptorByName(fk));
            }
            return pfds.toArray(new FieldDescriptor[] {});
        }

        return null;
    }

    private String getItemClass(ObjectReferenceDescriptor ord, CollectionDescriptor cld) {
        if (ord != null) {
            return ord.getItemClassName();
        } else if (cld != null) {
            cld.getItemClassName();
        }
        return null;
    }

    private boolean isMToN(CollectionDescriptor cld) {
        if (cld != null) {
            return cld.isMtoNRelation();
        }
        return false;
    }

    private boolean isAnonymousFk(FieldDescriptor fd) {
        if (fd != null) {
            return "anonymous".equals(fd.getAccess());
        }
        return false;
    }

    private boolean isNullableFk(FieldDescriptor fd) {
        if (fd != null) {
            return fd.isRequired();
        }
        return false;
    }
}