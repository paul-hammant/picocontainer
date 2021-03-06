/*****************************************************************************
 * Copyright (C) 2003-2010 PicoContainer Committers. All rights reserved.    *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Joerg Schaibe                                            *
 *****************************************************************************/

package org.picocontainer.behaviors;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.Decorator;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.Parameter;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;

import java.lang.reflect.Type;
import java.util.Properties;

/**
 * Behavior for Decorating. This factory will create {@link org.picocontainer.behaviors.Decorating.Decorated} that will
 * allow you to decorate what you like on the component instance that has been created
 *
 * @author Paul Hammant
 */
public abstract class Decorating extends AbstractBehavior implements Decorator {

    public <T> ComponentAdapter<T> createComponentAdapter(ComponentMonitor monitor, LifecycleStrategy lifecycle,
                                                   Properties componentProps, final Object key,
                                                   final Class<T> impl, final Parameter... parameters) throws PicoCompositionException {
        return monitor.changedBehavior(new Decorated<T>(super.createComponentAdapter(monitor, lifecycle,
                componentProps,key, impl, parameters), this));
    }

    @SuppressWarnings("serial")
    public static class Decorated<T> extends AbstractChangedBehavior<T> {
        private final Decorator decorator;

        public Decorated(ComponentAdapter<T> delegate, Decorator decorator) {
            super(delegate);
            this.decorator = decorator;
        }

        public T getComponentInstance(final PicoContainer container, Type into)
                throws PicoCompositionException {
            T instance = super.getComponentInstance(container, into);
            decorator.decorate(instance);
            return instance;
        }

        public String getDescriptor() {
            return "Decorated";
        }

    }

}