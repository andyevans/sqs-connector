
package org.mule.modules.sqs.adapters;

import javax.annotation.Generated;
import org.mule.api.MetadataAware;
import org.mule.modules.sqs.SQSConnector;


/**
 * A <code>SQSConnectorMetadataAdapater</code> is a wrapper around {@link SQSConnector } that adds support for querying metadata about the extension.
 * 
 */
@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-16T10:16:12-05:00", comments = "Build master.1915.dd1962d")
public class SQSConnectorMetadataAdapater
    extends SQSConnectorCapabilitiesAdapter
    implements MetadataAware
{

    private final static String MODULE_NAME = "Amazon SQS";
    private final static String MODULE_VERSION = "2.3.1-SNAPSHOT";
    private final static String DEVKIT_VERSION = "3.5.0-SNAPSHOT";
    private final static String DEVKIT_BUILD = "master.1915.dd1962d";
    private final static String MIN_MULE_VERSION = "3.5";

    public String getModuleName() {
        return MODULE_NAME;
    }

    public String getModuleVersion() {
        return MODULE_VERSION;
    }

    public String getDevkitVersion() {
        return DEVKIT_VERSION;
    }

    public String getDevkitBuild() {
        return DEVKIT_BUILD;
    }

    public String getMinMuleVersion() {
        return MIN_MULE_VERSION;
    }

}
