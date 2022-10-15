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
package com.hemajoo.commerce.plugin.rbt.action;

import com.hemajoo.commerce.plugin.rbt.message.MessageAbout;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.Icons;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Action associated to the {@code I18n Resource Bundle Translator} plugin used to invoke the {@code About} dialog.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public class ActionAbout extends AnAction
{
    /**
     * Creates a new action instance.
     */
    public ActionAbout()
    {
        super("About", "Show information about the Resource Bundle Translator (RBT) plugin.", Icons.ToolbarAbout);
    }

    /**
     * Performed each time the action is invoked.
     * @param event Action event.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event)
    {
        MessageAbout publisher = Objects.requireNonNull(event.getProject())
                .getMessageBus()
                .syncPublisher(MessageAbout.MESSAGE_TOPIC_ABOUT);

        publisher.about();
    }
}
