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
package com.hemajoo.commerce.plugin.rbt.message;

import com.hemajoo.commerce.plugin.rbt.action.ActionActivate;
import com.intellij.util.messages.Topic;

/**
 * Message associated to the to the {@link ActionActivate} action.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public interface MessageActivate
{
    /**
     * Message topic used to subscribe to {@link ActionActivate} invocation events.
     */
    Topic<MessageActivate> MESSAGE_TOPIC_ACTIVATE_TRANSLATOR = Topic.create("Resource Bundle Translator", MessageActivate.class);

    /**
     * Listener service used to notify subscribers of the {@link MessageActivate#MESSAGE_TOPIC_ACTIVATE_TRANSLATOR} that a
     * {@link ActionActivate} action has been invoked.
     */
    void fileSelected();
}
