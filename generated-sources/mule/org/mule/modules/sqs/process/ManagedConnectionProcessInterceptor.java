
package org.mule.modules.sqs.process;

import java.util.List;
import javax.annotation.Generated;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessInterceptor;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.devkit.processor.ExpressionEvaluatorSupport;
import org.mule.modules.sqs.adapters.SQSConnectorConnectionIdentifierAdapter;
import org.mule.modules.sqs.connection.ConnectionManager;
import org.mule.modules.sqs.connection.UnableToAcquireConnectionException;
import org.mule.modules.sqs.connection.UnableToReleaseConnectionException;
import org.mule.modules.sqs.connectivity.SQSConnectorConnectionKey;
import org.mule.modules.sqs.processors.ConnectivityProcessor;
import org.mule.security.oauth.callback.ProcessCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Generated(value = "Mule DevKit Version 3.5.0-SNAPSHOT", date = "2014-04-16T10:18:05-05:00", comments = "Build master.1915.dd1962d")
public class ManagedConnectionProcessInterceptor<T >
    extends ExpressionEvaluatorSupport
    implements ProcessInterceptor<T, SQSConnectorConnectionIdentifierAdapter>
{

    private static Logger logger = LoggerFactory.getLogger(ManagedConnectionProcessInterceptor.class);
    private final ConnectionManager<SQSConnectorConnectionKey, SQSConnectorConnectionIdentifierAdapter> connectionManager;
    private final MuleContext muleContext;
    private final ProcessInterceptor<T, SQSConnectorConnectionIdentifierAdapter> next;

    public ManagedConnectionProcessInterceptor(ProcessInterceptor<T, SQSConnectorConnectionIdentifierAdapter> next, ConnectionManager<SQSConnectorConnectionKey, SQSConnectorConnectionIdentifierAdapter> connectionManager, MuleContext muleContext) {
        this.next = next;
        this.connectionManager = connectionManager;
        this.muleContext = muleContext;
    }

    @Override
    public T execute(ProcessCallback<T, SQSConnectorConnectionIdentifierAdapter> processCallback, SQSConnectorConnectionIdentifierAdapter object, MessageProcessor messageProcessor, MuleEvent event)
        throws Exception
    {
        SQSConnectorConnectionIdentifierAdapter connection = null;
        SQSConnectorConnectionKey key = null;
        if (hasConnectionKeysOverride(messageProcessor)) {
            ConnectivityProcessor connectivityProcessor = ((ConnectivityProcessor) messageProcessor);
            final String _transformedAccessKey = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_accessKeyType"), null, connectivityProcessor.getAccessKey()));
            if (_transformedAccessKey == null) {
                throw new UnableToAcquireConnectionException("Parameter accessKey in method connect can't be null because is not @Optional");
            }
            final String _transformedSecretKey = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_secretKeyType"), null, connectivityProcessor.getSecretKey()));
            if (_transformedSecretKey == null) {
                throw new UnableToAcquireConnectionException("Parameter secretKey in method connect can't be null because is not @Optional");
            }
            final String _transformedQueueName = ((String) evaluateAndTransform(muleContext, event, connectivityProcessor.typeFor("_queueNameType"), null, connectivityProcessor.getQueueName()));
            key = new SQSConnectorConnectionKey(_transformedAccessKey, _transformedSecretKey, _transformedQueueName);
        } else {
            key = connectionManager.getEvaluatedConnectionKey(event);
        }
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(("Attempting to acquire connection using "+ key.toString()));
            }
            connection = connectionManager.acquireConnection(key);
            if (connection == null) {
                throw new UnableToAcquireConnectionException();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug((("Connection has been acquired with [id="+ connection.getConnectionIdentifier())+"]"));
                }
            }
            return next.execute(processCallback, connection, messageProcessor, event);
        } catch (Exception e) {
            if (processCallback.getManagedExceptions()!= null) {
                for (Class exceptionClass: ((List<Class<? extends Exception>> ) processCallback.getManagedExceptions())) {
                    if (exceptionClass.isInstance(e)) {
                        if (logger.isDebugEnabled()) {
                            logger.debug((((("An exception ( "+ exceptionClass.getName())+") has been thrown. Destroying the connection with [id=")+ connection.getConnectionIdentifier())+"]"));
                        }
                        try {
                            if (connection!= null) {
                                connectionManager.destroyConnection(key, connection);
                                connection = null;
                            }
                        } catch (Exception innerException) {
                            logger.error(innerException.getMessage(), innerException);
                        }
                    }
                }
            }
            throw e;
        } finally {
            try {
                if (connection!= null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug((("Releasing the connection back into the pool [id="+ connection.getConnectionIdentifier())+"]"));
                    }
                    connectionManager.releaseConnection(key, connection);
                }
            } catch (Exception e) {
                throw new UnableToReleaseConnectionException(e);
            }
        }
    }

    /**
     * Validates that the current message processor has changed any of its connection parameters at processor level. If so, a new SQSConnectorConnectionKey must be generated
     * 
     * @param messageProcessor
     *     the message processor to test against the keys
     * @return
     *     true if any of the parameters in @Connect method annotated with @ConnectionKey was override in the XML, false otherwise  
     */
    private Boolean hasConnectionKeysOverride(MessageProcessor messageProcessor) {
        if ((messageProcessor == null)||(!(messageProcessor instanceof ConnectivityProcessor))) {
            return false;
        }
        ConnectivityProcessor connectivityProcessor = ((ConnectivityProcessor) messageProcessor);
        if (connectivityProcessor.getAccessKey()!= null) {
            return true;
        }
        return false;
    }

    public T execute(ProcessCallback<T, SQSConnectorConnectionIdentifierAdapter> processCallback, SQSConnectorConnectionIdentifierAdapter object, Filter filter, MuleMessage message)
        throws Exception
    {
        throw new UnsupportedOperationException();
    }

}
