package org.jodaengine.ext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jodaengine.deployment.importer.bpmn.BpmnXmlParseListener;
import org.jodaengine.plugin.activity.ActivityLifecyclePlugin;

/**
 * This annotation identifies the extension parts of this engine.
 * 
 * Those extensions are typically located in a different project and implement certain
 * listener APIs, such as {@link BpmnXmlParseListener} or {@link ActivityLifecyclePlugin}.
 * 
 * @author Jan Rehwaldt
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Extension {
    
}
