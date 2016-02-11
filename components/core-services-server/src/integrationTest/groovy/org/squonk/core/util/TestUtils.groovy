package org.squonk.core.util

import javax.sql.DataSource

import com.im.lac.job.jobdef.JobStatus
import com.im.lac.services.dataset.service.*
import org.squonk.core.job.Job

/**
 *
 * @author timbo
 */
class TestUtils {

    public final static String TEST_USERNAME = 'squonkuser'
        
//    static List<Long> createTestData(DatasetHandler handler) {
//
//        def ids = []
//
//        ids << handler.createDataset('World', 'test0').id
//        ids << handler.createDataset(["one", "two", "three"],'test1').id
//        ids << handler.createDataset(["red", "yellow", "green", "blue"],'test2').id
//        ids << handler.createDataset(["banana", "pineapple", "orange", "apple", "pear"], 'test3').id
//        
//        return ids
//    }
    
    static DataSource createTestDataSource() {
        return Utils.createDataSource()
    }
    
    static void waitForJobToComplete(Job job, long timeOutMillis) {
        long t0 = System.currentTimeMillis()
        long t1 = t0
        JobStatus status = job.getCurrentJobStatus()
        while (!(status.status == JobStatus.Status.COMPLETED || status.status == JobStatus.Status.ERROR)) {
            t1 = System.currentTimeMillis()
            if ((t1 - t0) > timeOutMillis) {
                break
            }
            sleep(100)
            status = job.getCurrentJobStatus()
        }
        //println "Completed in " + (t1 - t0) + " millis"
    }
    
	
}
