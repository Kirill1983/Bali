/* 
 * Copyright © 2011 Konoplev Kirill
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kkonoplev.bali.taglib;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to represent a menu.
 * 
 */
public class MenuAc {

    private String      _id;
    private String      _nameKey;
    private String      _altTextKey;
    private String      _imageSrc;
    private String      _action;
    private boolean     _disabled;
    private boolean     _small;
    private boolean     _isSpacer;
    private List<MenuAc> _children;

    /**
     * Constructor for menu.
     * @param id the id of the menu.
     * @param nameKey the resource key for name.
     * @param action the action for the menu.
     */
    public MenuAc(String id, String nameKey, String action) {
        _id = id;
        _nameKey = nameKey;
        _action = action;
        _disabled = false;
    }

    /**
     * Constructor for menu.
     * @param id the id of the menu.
     * @param nameKey the resource key for name.
     * @param altTextKey the resource key for alt text.
     * @param action the action for the menu.
     */
    public MenuAc(String id, String nameKey, String altTextKey, String action) {
        this(id, nameKey, action);
        _altTextKey = altTextKey;
    }

    /**
     * Constructor for spacer menu.
     * @param isSpacer this should be set to true.
     */
    public MenuAc(boolean isSpacer) {
        _isSpacer = isSpacer;
    }

    
    /**
     * Get the id for the menu.
     * @return the id for the menu.
     */
    public String getId() {
        return _id;
    }

    /**
     * Get the resource key for menu name.
     * @return the resource key for menu name.
     */
    public String getNameKey() {
        return _nameKey;
    }

    /**
     * Get the resource key for menu alt text.
     * @return the resource key for menu alt text.
     */
    public String getAltTextKey() {
        return _altTextKey;
    }

    /**
     * Get the src for the image associated with the menu.
     * @return the src for the image associated with the menu.
     */
    public String getImageSrc() {
        return _imageSrc;
    }

    /**
     * Check if the given menu is disabled.
     * @return true if the menu is disabled.
     */
    public boolean getDisabled() {
        return _disabled;
    }

    /**
     * Check if the given menu is a spacer and does not have any content.
     * @return true if the menu is spacer.
     */
    public boolean isSpacer() {
        return _isSpacer;
    }
    
    /**
     * Check if the menu is a small menu.
     * @return true if the menu is a small menu.
     */
    public boolean getSmall() {
        return _small;
    }
    
    /**
     * Get the action for the menu.
     * @return the action for the menu.
     */
    public String getAction() {
        return _action;
    }

    /**
     * Get the list of children as {@link Menu}s.
     * @return the list of children as {@link Menu}s.
     */
    public List<MenuAc> getChildren() {
        return _children;
    }


    /**
     * Set the dom id for the menu.
     * @param value the dom id for the menu.
     */
    public void setId(String value) {
        _id = value;
    }

    /**
     * Set the resource key for the name.
     * @param value the resource key for the name.
     */
    public void setNameKey(String value) {
        _nameKey = value;
    }

    /**
     * Set the resource key for the alt text.
     * @param value the resource key for the alt text.
     */
    public void setAltTextKey(String value) {
        _altTextKey = value;
    }

    /**
     * Set the image src for the menu.
     * @param value the image src for the menu.
     */
    public void setImageSrc(String value) {
        _imageSrc = value;
    }

    /**
     * Set to true if the menu is disabled.
     * @param value if true, the menu is disabled.
     */
    public void setDisabled(boolean value) {
        _disabled = value;
    }
    
    /**
     * Set to true if the menu is a spacer.
     * @param value if true, the menu is a spacer.
     */
    public void setSpacer(boolean value) {
        _isSpacer = value;
    }
    
    /**
     * Set to true if a small menu should be renderd.
     * @param value if true, a small menu is rendered.
     */
    public void setSmall(boolean value) {
        _small = value;
    }

    /**
     * Set the action for the menu.
     * @param value the action for the menu.
     */
    public void setAction(String value) {
        _action = value;
    }

    /**
     * Set the list of children for this menu.
     * @param list the list of {@link Menu}.
     */
    public void setChildren(List<MenuAc> list) {
        _children = list;
    }

    /**
     * Set the child menu for this menu.
     * @param menu the child menu as {@link Menu}.
     */
    public void addChild(MenuAc menu) {
        if (_children == null) {
            _children = new ArrayList<MenuAc>();
        }
        _children.add(menu);
    }
}
