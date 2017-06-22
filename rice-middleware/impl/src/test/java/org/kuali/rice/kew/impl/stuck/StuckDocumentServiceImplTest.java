package org.kuali.rice.kew.impl.stuck;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link StuckDocumentServiceImpl}
 *
 * @author Eric Westfall
 */
@RunWith(MockitoJUnitRunner.class)
public class StuckDocumentServiceImplTest {

    @Mock
    private StuckDocumentDao dao;

    @InjectMocks
    private StuckDocumentServiceImpl stuckDocumentService;

    private Map<String, StuckDocumentIncident> incidentDatabase;
    private Map<String, StuckDocumentFixAttempt> fixAttemptDatabase;

    @Before
    public void setup() {
        incidentDatabase = new HashMap<String, StuckDocumentIncident>();
        fixAttemptDatabase = new HashMap<String, StuckDocumentFixAttempt>();
        when(dao.findIncident(any())).then(invocation -> {
            return incidentDatabase.get(invocation.getArgumentAt(0, String.class));
        });
        when(dao.saveIncident(any())).then(invocation -> {
            StuckDocumentIncident incident = invocation.getArgumentAt(0, StuckDocumentIncident.class);
            if (incident.getStuckDocumentIncidentId() == null) {
                incident.setStuckDocumentIncidentId(UUID.randomUUID().toString());
            }
            incidentDatabase.put(incident.getStuckDocumentIncidentId(), incident);
            return incident;
        });
        when(dao.saveFixAttempt(any())).then(invocation -> {
            StuckDocumentFixAttempt attempt = invocation.getArgumentAt(0, StuckDocumentFixAttempt.class);
            if (attempt.getStuckDocumentFixAttemptId() == null) {
                attempt.setStuckDocumentFixAttemptId(UUID.randomUUID().toString());
            }
            fixAttemptDatabase.put(attempt.getStuckDocumentFixAttemptId(), attempt);
            return attempt;
        });
    }

    @Test(expected = NullPointerException.class)
    public void testFind_NullArgument() {
        stuckDocumentService.find(null);
    }

    @Test
    public void testFind() {
        List<StuckDocumentIncident> incidents = generateAndSaveIncidents(2);

        String incidentId = UUID.randomUUID().toString();
        assertNull(stuckDocumentService.find(incidentId));
        verify(dao, times(1)).findIncident(incidentId);

        incidents.forEach(incident -> assertEquals(incident, stuckDocumentService.find(incident.getStuckDocumentIncidentId())));
    }

    @Test(expected = NullPointerException.class)
    public void testFindAll_NullArgument() {
        stuckDocumentService.findAll(null);
    }

    @Test
    public void testFindAll_EmptyList() {
        assertTrue(stuckDocumentService.findAll(Collections.emptyList()).isEmpty());
    }

    @Test
    public void testFindAll_BadId() {
        // it should just ignore bad ids
        List<StuckDocumentIncident> generatedIncidents = generateAndSaveIncidents(5);

        List<String> incidentIds = new ArrayList<>();
        incidentIds.add(UUID.randomUUID().toString());
        List<StuckDocumentIncident> incidents = stuckDocumentService.findAll(incidentIds);
        assertEquals(0, incidents.size());

        // now have list with one good id and one bad one
        incidentIds.add(generatedIncidents.get(1).getStuckDocumentIncidentId());
        incidents = stuckDocumentService.findAll(incidentIds);
        assertEquals(1, incidents.size());
    }

    @Test
    public void testFindAll() {
        List<StuckDocumentIncident> incidents = generateAndSaveIncidents(5);
        StuckDocumentIncident incident1 = incidents.get(0);
        StuckDocumentIncident incident2 = incidents.get(2);
        StuckDocumentIncident incident3 = incidents.get(4);

        List<String> incidentIdsToFind = new ArrayList<>();
        incidentIdsToFind.add(incident1.getStuckDocumentIncidentId());
        incidentIdsToFind.add(incident2.getStuckDocumentIncidentId());
        incidentIdsToFind.add(incident3.getStuckDocumentIncidentId());

        List<StuckDocumentIncident> results = stuckDocumentService.findAll(incidentIdsToFind);
        assertEquals(3, results.size());
        assertTrue(results.contains(incident1));
        assertTrue(results.contains(incident2));
        assertTrue(results.contains(incident3));
    }

    @Test
    public void testIdentifyAndRecordNewStuckDocuments_NoStuckDocuments() {
        when(dao.identifyNewStuckDocuments()).thenReturn(Collections.emptyList());
        assertTrue(stuckDocumentService.identifyAndRecordNewStuckDocuments().isEmpty());
        verify(dao, times(1)).identifyNewStuckDocuments();
        verifyNoMoreInteractions(dao);
    }

    @Test
    public void testIdentifyAndRecordNewStuckDocuments() {
        // identify some doc ids and return them from dao.identityNewStuckDocuments
        String documentId1 = "123456789";
        String documentId2 = "987654321";
        List<String> stuckDocumentIds = Lists.newArrayList(documentId1, documentId2);
        when(dao.identifyNewStuckDocuments()).thenReturn(stuckDocumentIds);


        List<StuckDocumentIncident> newIncidents = stuckDocumentService.identifyAndRecordNewStuckDocuments();
        assertEquals(2, newIncidents.size());
        List<String> newIncidentDocIds = newIncidents.stream().map(StuckDocumentIncident::getDocumentId).collect(Collectors.toList());
        assertTrue(newIncidentDocIds.contains(documentId1));
        assertTrue(newIncidentDocIds.contains(documentId2));

        newIncidents.forEach(newIncident -> {
            assertNotNull(newIncident.getStuckDocumentIncidentId());
            assertNotNull(newIncident.getDocumentId());
            assertNotNull(newIncident.getStartDate());
            assertEquals(StuckDocumentIncident.Status.PENDING, newIncident.getStatus());
            assertNull(newIncident.getEndDate());
        });

        verify(dao, times(1)).identifyNewStuckDocuments();
        verify(dao, times(2)).saveIncident(any());
        verifyNoMoreInteractions(dao);
    }

    @Test(expected = NullPointerException.class)
    public void testRecordNewFixAttempt_NullArgument() {
        stuckDocumentService.recordNewFixAttempt(null);
    }

    @Test
    public void testRecordNewFixAttempt() {
        StuckDocumentIncident incident = generateAndSaveIncident();

        StuckDocumentFixAttempt attempt1 = stuckDocumentService.recordNewFixAttempt(incident);
        assertNotNull(attempt1.getStuckDocumentFixAttemptId());
        assertEquals(attempt1, fixAttemptDatabase.get(attempt1.getStuckDocumentFixAttemptId()));
        // make sure the timestamp is no more than 1 second ago
        assertRecentTimestamp(attempt1.getTimestamp());
        assertEquals(incident.getStuckDocumentIncidentId(), attempt1.getStuckDocumentIncidentId());

        // now try to record another one for the same doc
        StuckDocumentFixAttempt attempt2 = stuckDocumentService.recordNewFixAttempt(incident);
        assertNotEquals(attempt1, attempt2);
        assertNotNull(attempt2.getStuckDocumentFixAttemptId());
        assertEquals(attempt2, fixAttemptDatabase.get(attempt2.getStuckDocumentFixAttemptId()));
        // make sure the timestamp is no more than 1 second ago
        assertRecentTimestamp(attempt2.getTimestamp());
        assertEquals(incident.getStuckDocumentIncidentId(), attempt2.getStuckDocumentIncidentId());

    }

    @Test(expected = NullPointerException.class)
    public void testResolveIfPossible_NullArgument() {
        stuckDocumentService.resolveIfPossible(null);
    }

    @Test
    public void testResolveIfPossible_EmptyList() {
        when(dao.identifyStillStuckDocuments(any())).thenReturn(Collections.emptyList());
        assertTrue(stuckDocumentService.resolveIfPossible(Collections.emptyList()).isEmpty());
    }

    @Test
    public void testResolveIfPossible() {
        List<StuckDocumentIncident> stuck = generateAndSaveIncidents(3);
        StuckDocumentIncident incident1 = stuck.get(0);
        StuckDocumentIncident incident2 = stuck.get(1);
        StuckDocumentIncident incident3 = stuck.get(2);

        // let's pretend like the first and second incident are no longer stuck
        when(dao.identifyStillStuckDocuments(any())).thenReturn(Lists.newArrayList(incident3));

        List<StuckDocumentIncident> stillStuck = stuckDocumentService.resolveIfPossible(Lists.newArrayList(
                incident1.getStuckDocumentIncidentId(),
                incident2.getStuckDocumentIncidentId(),
                incident3.getStuckDocumentIncidentId()));

        assertEquals(1, stillStuck.size());
        assertEquals(incident3.getStuckDocumentIncidentId(), stillStuck.get(0).getStuckDocumentIncidentId());
        StuckDocumentIncident stillStuckIncident = stillStuck.get(0);
        assertNotEquals(StuckDocumentIncident.Status.FIXED, stillStuckIncident.getStatus());

        // now check the other two and make sure they've been updated
        StuckDocumentIncident incident1Updated = stuckDocumentService.find(incident1.getStuckDocumentIncidentId());
        StuckDocumentIncident incident2Updated = stuckDocumentService.find(incident2.getStuckDocumentIncidentId());
        assertEquals(StuckDocumentIncident.Status.FIXED, incident1Updated.getStatus());
        assertEquals(StuckDocumentIncident.Status.FIXED, incident2Updated.getStatus());
        // check that the end dates were recorded within the last second
        assertRecentTimestamp(incident1Updated.getEndDate());
        assertRecentTimestamp(incident2Updated.getEndDate());
    }

    @Test(expected = NullPointerException.class)
    public void testResolve_NullArgument() {
        stuckDocumentService.resolve(null);
    }

    @Test
    public void testResolve() {
        StuckDocumentIncident incident = generateAndSaveIncident();
        assertEquals(StuckDocumentIncident.Status.PENDING, incident.getStatus());
        assertNull(incident.getEndDate());

        // now resolve it
        incident = stuckDocumentService.resolve(incident);
        assertEquals(StuckDocumentIncident.Status.FIXED, incident.getStatus());
        assertRecentTimestamp(incident.getEndDate());

        verify(dao, times(2)).saveIncident(any());
    }

    @Test(expected = NullPointerException.class)
    public void testStartFixing_NullArgument() {
        stuckDocumentService.startFixing(null);
    }

    @Test
    public void testStartFixing() {
        StuckDocumentIncident incident = generateAndSaveIncident();
        assertEquals(StuckDocumentIncident.Status.PENDING, incident.getStatus());

        // now start fixing it
        incident = stuckDocumentService.startFixing(incident);
        assertEquals(StuckDocumentIncident.Status.FIXING, incident.getStatus());

        verify(dao, times(2)).saveIncident(any());
    }

    @Test(expected = NullPointerException.class)
    public void testRecordFailure_NullArgument() {
        stuckDocumentService.recordFailure(null);
    }

    @Test
    public void testRecordFailure() {
        StuckDocumentIncident incident = generateAndSaveIncident();
        assertEquals(StuckDocumentIncident.Status.PENDING, incident.getStatus());
        assertNull(incident.getEndDate());

        // now resolve it
        incident = stuckDocumentService.recordFailure(incident);
        assertEquals(StuckDocumentIncident.Status.FAILED, incident.getStatus());
        assertRecentTimestamp(incident.getEndDate());

        verify(dao, times(2)).saveIncident(any());
    }

    private List<StuckDocumentIncident> generateAndSaveIncidents(int numberOfIncidents) {
        List<StuckDocumentIncident> incidents = Lists.newArrayList();
        for (int i = 0; i < numberOfIncidents; i++) {
            incidents.add(generateAndSaveIncident());
        }
        return incidents;
    }

    private StuckDocumentIncident generateAndSaveIncident() {
        return dao.saveIncident(StuckDocumentIncident.startNewIncident(UUID.randomUUID().toString()));
    }

    private void assertRecentTimestamp(Timestamp timestamp) {
        long diff = System.currentTimeMillis() - timestamp.getTime();
        assertTrue(diff >= 0 && diff < 1000);
    }


}
