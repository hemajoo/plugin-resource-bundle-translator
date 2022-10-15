/*
 * Copyright(c) 2020 by Resse Christophe.
 * --------------------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -------------------------------------------------------------------------------------
 */
package com.hemajoo.commerce.plugin.rbt.model;

import org.ressec.core.foundation.exception.AbstractCheckedException;

/**
 * Checked exception thrown to indicate an error occurred with a {@link PropertiesModel} entity.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public class PropertiesModelException extends AbstractCheckedException
{
    /**
     * Default serialization identifier.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Thrown to indicate that an error occurred with a {@link PropertiesModel} entity.
     * @param exception Parent {@link Exception}.
     */
    public PropertiesModelException(final Exception exception)
    {
        super(exception);
    }

    /**
     * Thrown to indicate that an error occurred with a {@link PropertiesModel} entity.
     * @param message Message describing the error being the cause of the raised exception.
     */
    public PropertiesModelException(final String message)
    {
        super(message);
    }

    /**
     * Thrown to indicate that an error occurred with a {@link PropertiesModel} entity.
     * @param message Message describing the error being the cause of the raised exception.
     * @param exception Parent {@link Exception}.
     */
    public PropertiesModelException(final String message, final Exception exception)
    {
        super(message, exception);
    }
}
