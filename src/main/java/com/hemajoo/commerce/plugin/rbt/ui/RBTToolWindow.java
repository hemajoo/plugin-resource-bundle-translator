/*
 * Copyright(c) 2020 by Resse Christophe.
 * --------------------------------------------------------------------------------------
 * This file is part of Resse Christophe public projects which is licensed
 * under the Apache license version 2 and use is subject to license terms.
 * You should have received a copy of the license with the project's artifact
 * binaries and/or sources.
 *
 * License can be consulted at http://www.apache.org/licenses/LICENSE-2.0
 * --------------------------------------------------------------------------------------
 */
package com.hemajoo.commerce.plugin.rbt.ui;

import com.hemajoo.commerce.plugin.rbt.action.ActionAbout;
import com.hemajoo.commerce.plugin.rbt.action.ActionSelectAll;
import com.hemajoo.commerce.plugin.rbt.action.ActionTranslate;
import com.hemajoo.commerce.plugin.rbt.action.ActionUnselectAll;
import com.hemajoo.commerce.plugin.rbt.message.*;
import com.hemajoo.commerce.plugin.rbt.model.PropertiesModelException;
import com.hemajoo.commerce.plugin.rbt.model.TranslationFile;
import com.hemajoo.commerce.plugin.rbt.service.RBTService;
import com.hemajoo.commerce.plugin.rbt.util.HTMLString;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.CheckBoxList;
import lombok.NonNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Main window of the {@code Resource Bundle Translator}.
 * @author <a href="mailto:christophe.resse@gmail.com">Christophe Resse</a>
 * @version 1.0.0
 */
public class RBTToolWindow
{
    private JPanel mainPanel;
    private JPanel upSplitPanel;
    private JPanel bottomSplitPanel;
    private JSplitPane splitPane;
    private JPanel toolbarPanel;
    private CheckBoxList<JCheckBox> targetTranslationList;
    private CheckBoxList<JCheckBox> sourceTranslationList;
    private final Project project;

    /**
     * Creates a new {@code I18nWindow}.
     * @param window Tool window.
     * @param project Current project.
     */
    public RBTToolWindow(ToolWindow window, Project project)
    {
        this.project = project;

        createToolbar();

        // Register messages for toolbar buttons.
        project.getMessageBus().connect().subscribe(MessageActivate.MESSAGE_TOPIC_ACTIVATE_TRANSLATOR, this::onFileSelectedMessageReceived);
        project.getMessageBus().connect().subscribe(MessageUnselectAll.MESSAGE_TOPIC_UNSELECT, this::onUnselectMessageReceived);
        project.getMessageBus().connect().subscribe(MessageTranslate.MESSAGE_TOPIC_TRANSLATE, this::onTranslateMessageReceived);
        project.getMessageBus().connect().subscribe(MessageSelectAll.MESSAGE_TOPIC_SELECT_ALL_TARGET, this::onSelectAllTargetFiles);
        project.getMessageBus().connect().subscribe(MessageAbout.MESSAGE_TOPIC_ABOUT, this::onAbout);
    }

    private void onAbout()
    {
        new AboutDialogWrapper().showAndGet();
    }

    private void onSelectAllTargetFiles()
    {
        DefaultListModel<JCheckBox> model = new DefaultListModel<>();
        JCheckBox checkbox;

        targetTranslationList.clear();

        for (Map.Entry<Locale, TranslationFile> entry : getService().getModel().getTargetTranslation().entrySet())
        {
            if (!entry.getKey().equals(getService().getModel().getSourceTranslation().getLocale()))
            {
                checkbox = new JCheckBox(entry.getValue().getUILabel(), true);
                checkbox.addItemListener(this::onTargetFileChanged);
                model.addElement(checkbox);
                getService().getModel().selectTarget( getPureText(checkbox.getText()));
            }
        }

        targetTranslationList.setModel(model);
    }

    /**
     * Creates a toolbar.
     */
    private void createToolbar()
    {
        DefaultActionGroup actionGroup = new DefaultActionGroup("I18nActionGroup", false);
        actionGroup.add(new ActionTranslate());
        actionGroup.add(new ActionSelectAll());
        actionGroup.add(new ActionUnselectAll());
        actionGroup.add(new ActionAbout());

        ActionManager actionManager = ActionManager.getInstance();
        ActionToolbar actionToolbar = actionManager.createActionToolbar("I18nToolbar", actionGroup, true);
        toolbarPanel.add(actionToolbar.getComponent(), BorderLayout.WEST);
    }

    /**
     * Invoked each time a {@link MessageTranslate#MESSAGE_TOPIC_TRANSLATE} message is received.
     */
    private void onTranslateMessageReceived()
    {
        getService().execute();
        onUnselectMessageReceived();
    }

    /**
     * Invoked each time a {@link MessageActivate#MESSAGE_TOPIC_ACTIVATE_TRANSLATOR} message is received.
     */
    private void onFileSelectedMessageReceived()
    {
        JCheckBox checkBox;
        DefaultListModel<JCheckBox> uiModel = new DefaultListModel<>();

        for (Map.Entry<Locale, TranslationFile> entry : getService().getModel().getTranslations().entrySet())
        {
            checkBox = new JCheckBox(entry.getValue().getUILabel());
            checkBox.addItemListener(this::onSourceFileChanged);
            uiModel.addElement(checkBox);
        }
        sourceTranslationList.setModel(uiModel);
        targetTranslationList.clear();
    }

    /**
     * Invoked each time a {@link MessageUnselectAll#MESSAGE_TOPIC_UNSELECT} message is received.
     * <br>
     * It clears all files from the file lists.
     */
    private void onUnselectMessageReceived()
    {
        RBTService service = Objects.requireNonNull(project).getService(RBTService.class);

        targetTranslationList.clear();
        service.getModel().getTargetTranslationSelected().clear();
        updateTargetUIList();
    }

    /**
     * Invoked when a source translation file selection changed (checked / unchecked).
     * @param e Item event.
     */
    private void onSourceFileChanged(ItemEvent e)
    {
        String name;

        if (e.getStateChange() == ItemEvent.SELECTED)
        {
            name = getPureText(((JCheckBox) e.getSource()).getText());
            try
            {
                getService().getModel().sourceTranslationChanged(name);
                updateTargetUIList();

                // If another checkbox was previously selected, then deselect it.
                updateSourceUIList(name);
            }
            catch (PropertiesModelException propertiesModelException)
            {
                propertiesModelException.printStackTrace();
            }
        }
    }

    private void updateSourceUIList(final String reference)
    {
        JCheckBox element;
        String name;

        ListModel<JCheckBox> model = sourceTranslationList.getModel();
        for (int i = 0; i < model.getSize(); i++)
        {
            element = model.getElementAt(i);
            name = getPureText(element.getText());
            if (!name.equals(reference))
            {
                element.setSelected(false);
            }
        }
    }

    private void updateTargetUIList()
    {
        DefaultListModel<JCheckBox> model = new DefaultListModel<>();
        JCheckBox checkBox;

        targetTranslationList.clear();

        for (Map.Entry<Locale, TranslationFile> entry : getService().getModel().getTargetTranslation().entrySet())
        {
            if (!entry.getKey().equals(getService().getModel().getSourceTranslation().getLocale()))
            {
                checkBox = new JCheckBox(entry.getValue().getUILabel());
                checkBox.addItemListener(this::onTargetFileChanged);
                model.addElement(checkBox);
            }
        }

        targetTranslationList.setModel(model);
    }

    /**
     * A target translation properties file selection has changed (checked / unchecked).
     * @param event Item event.
     */
    private void onTargetFileChanged(ItemEvent event)
    {
        JCheckBox checkbox;
        String name;
        ListModel<JCheckBox> model = targetTranslationList.getModel();

        getService().getModel().getTargetTranslationSelected().clear();

        for (int i = 0; i < model.getSize(); i++)
        {
            checkbox = model.getElementAt(i);
            name = getPureText(checkbox.getText());
            if (checkbox.isSelected())
            {
                getService().getModel().selectTarget(name);
            }
            else
            {
                getService().getModel().unselectTarget(name);
            }
        }
    }

    /**
     * Returns the main panel of the tool window.
     * @return Main panel component.
     */
    public JComponent getContent()
    {
        return mainPanel;
    }

    /**
     * Returns the I18n service.
     * @return I18n service.
     */
    public RBTService getService()
    {
        return Objects.requireNonNull(project).getService(RBTService.class);
    }

    public String getPureText(final @NonNull String value)
    {
        String[] parts = value.split(" \\(");
        return parts.length > 0 ? HTMLString.removeHtml(parts[0]) : HTMLString.removeHtml(value);
    }
}
