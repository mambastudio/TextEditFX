/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx.attributes;

import java.util.HashMap;

/**
 *
 * @author jmburu
 */
public class AttributeMap {
    HashMap<Object, Object> attributes;
    
    public AttributeMap()
    {
        attributes = new HashMap();
    }
    
    public void addAttribute(Object name, Object value)
    {
        attributes.put(name, value);
    }
    
    public boolean containsAttribute(Object name, Object value)
    {
        return false;
    }
}
