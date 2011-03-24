package de.hpi.oryxengine.correlation.adapter.mail;

import java.util.Properties;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.correlation.adapter.AbstractAdapterConfiguration;
import de.hpi.oryxengine.correlation.adapter.AdapterTypes;
import de.hpi.oryxengine.correlation.adapter.PullAdapterConfiguration;

/**
 * The mail adapter configuration.
 */
public final class MailAdapterConfiguration
extends AbstractAdapterConfiguration
implements PullAdapterConfiguration {
    
    /** The user name. */
    private final String userName;
    
    /** The password. */
    private final String password;
    
    /** The address. */
    private final String address;
    
    /** The port. */
    private final int port;
    
    /** The use ssl. */
    private final boolean useSSL;
    
    /** The type. */
    private final MailProtocol protocol;
    
    /** The Constant DEFAULT_INTERVAL. */
    private static final long DEFAULT_INTERVAL = 1000L;
    
    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Default constructor.
     * 
     * @param protocol the mail protocol
     * @param userName the account's user name
     * @param password the account's password
     * @param address the mail server's address
     * @param port the mail server's port
     * @param useSSL should ssl be used for connection
     */
    public MailAdapterConfiguration(@Nonnull MailProtocol protocol,
                                    @Nonnull String userName,
                                    @Nonnull String password,
                                    @Nonnull String address,
                                    @Nonnegative int port,
                                    @Nonnull boolean useSSL) {
        super(AdapterTypes.Mail);
        this.protocol = protocol;
        this.userName = userName;
        this.password = password;
        this.address = address;
        this.port = port;
        this.useSSL = useSSL;
    }
    
    /**
     * Returns the user name.
     * 
     * @return the user name
     */
    public @Nonnull String getUserName() {
        return userName;
    }
    
    /**
     * Returns the password.
     * 
     * @return the password.
     */
    public @Nonnull String getPassword() {
        return password;
    }
    
    /**
     * Returns the address. If javax.mail is used,
     * it is recommended to use toCon
     * 
     * @return the address
     */
    public @Nonnull String getAddress() {
        return address;
    }
    
    /**
     * Returns the port number.
     * 
     * @return the port number
     */
    public @Nonnegative int getPort() {
        return port;
    }
    
    /**
     * Creates a property set configured with the corresponding
     * settings.
     * 
     * @return a properties instance
     */
    public @Nonnull Properties toMailProperties() {
        // create the properties for the Session
        Properties props = new Properties();
        
        String identifier = "";
        switch (this.protocol) {
            case IMAP:
                identifier = "imap";
                break;
            case POP3:
                identifier = "pop3";
                break;
            default:
                logger.error("No supported mail type configured: {}", this.protocol);
        }
        
        if (this.useSSL) {
            // set this session up to use SSL for IMAP connections
            props.setProperty("mail." + identifier + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            // don't fallback to normal IMAP connections on failure.
            props.setProperty("mail." + identifier + ".socketFactory.fallback", "false");
        }
        
        // use the simap port for imap/ssl connections.
        props.setProperty("mail." + identifier + ".socketFactory.port", String.valueOf(this.port));
        props.setProperty("mail." + identifier + ".port", String.valueOf(this.port));
        
        // note that you can also use the defult imap port (including the
        // port specified by mail.imap.port) for your SSL port configuration.
        // however, specifying mail.imap.socketFactory.port means that,
        // if you decide to use fallback, you can try your SSL connection
        // on the SSL port, and if it fails, you can fallback to the normal
        // IMAP port.
        
        return props;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public long getPullInterval() {
        return DEFAULT_INTERVAL;
    }
    
    /**
     * Returns, whether to use ssl.
     * 
     * @return should use ssl
     */
    public boolean isUseSSL() {
        return useSSL;
    }
    
    /**
     * Returns the account's protocol.
     * 
     * @return the account protocol
     */
    public MailProtocol getProtocol() {
        return protocol;
    }

    @Override
    public String getUniqueName() {
        return String.format("%s:%s:%s:%s", protocol, address, port, userName);
    }
    
    /**
     * Dalmatina google configuration. This is to prevent code duplication. Remove this later.
     *
     * @return the mail adapter configuration
     */
    public static MailAdapterConfiguration dalmatinaGoogleConfiguration() {
        return new MailAdapterConfiguration(MailProtocol.IMAP, "oryxengine", "dalmatina!",
            "imap.googlemail.com", MailProtocol.IMAP.getPort(true), true);
    }
}
