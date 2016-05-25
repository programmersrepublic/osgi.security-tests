package org.osgi.security.sysatt110.test;

import org.osgi.framework.BundleContext;
import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.BundleActivator;

import org.osgi.security.util.api.Util;
import org.osgi.util.tracker.ServiceTracker;

import java.io.File;

/**
 * 
 **/

public class Activator implements BundleActivator
{
	private static BundleContext bundleContext;
	private ServiceTracker serviceRef;
    private Util util;
    private boolean succeed = false;

	static BundleContext getContext()
    {
		return bundleContext;
    }

	public void start(BundleContext context) throws Exception
  	{
  		Activator.bundleContext = context;
    }
  	
  	public void stop(BundleContext context) throws Exception
  	{
  		Activator.bundleContext = null;
    }
  	
	private void unregisterService()
	{
  		util.stop(succeed);
	}
  	
  	@Test
    public void testSysatt110() throws Exception
    {
  		serviceRef = new ServiceTracker(getContext(), Util.class.getName(), null);
  		serviceRef.open();
  		util = (Util) serviceRef.waitForService(30000); 
  		
  		Assert.assertNotNull("JUnit test issue: util service is not available even after 30 s", util);
  		util.start("sysatt110","Log files exfiltration as a standard user","Access to pertinent system log directories (ex: /var/log on Linux OS)");
  		
  		util.print("\n[TEST] Checking access to /var/log directory... ");
  		File dir = new File("/var/log/");
		File list[] ;
		list = dir.listFiles();
		
		//Assert.assertNull("JUnit test issue: logs are accessible by any bundle.", list);
		
		if (list != null)
		{
			util.println("KO\n");
			Assert.fail("JUnit test issue: logs are accessible by any bundle.");
		}
		else
		{
			util.println("OK\n");
		}
		
		succeed = true;
	    unregisterService();
    }
}