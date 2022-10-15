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
package com.hemajoo.commerce.plugin.rbt.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.ContentManagerEvent;
import com.intellij.ui.content.ContentManagerListener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Factory for creating windows for the {@code Resource Bundle Translator} plugin.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public final class RBTToolWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory
{
  /**
   * Create the tool window content.
   * @param project current project
   * @param toolWindow current tool window
   */
  private void addEmptyContent(@NotNull Project project, @NotNull ToolWindow toolWindow)
  {
    RBTToolWindow contentWindow = new RBTToolWindow(toolWindow, project);

    ContentManager contentManager = toolWindow.getContentManager();
    Content content = contentManager.getFactory().createContent(contentWindow.getContent(), "Properties", false);
    contentManager.addContent(content);
  }

  @Override
  public boolean isApplicable(@NotNull Project project)
  {
    return true;
  }

  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow)
  {
    toolWindow.setToHideOnEmptyContent(true);

    toolWindow.addContentManagerListener(new ContentManagerListener()
    {
      @Override
      public void contentRemoved(@NotNull ContentManagerEvent event)
      {
        if (Objects.requireNonNull(event.getContent().getManager()).getContentCount() == 0)
        {
          addEmptyContent(project, toolWindow);
        }
      }
    });

    addEmptyContent(project, toolWindow);
  }

}
