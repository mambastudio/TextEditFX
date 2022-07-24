/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx.model;

import texteditfx.attributes.AttributeMap;
import texteditfx.text.Position;
import texteditfx.text.Segment;

/**
 *
 * @author jmburu
 */
public interface Document {
    public int getLength();
    public String getText();
    public void getText(int offset, int length, Segment segment);
    public void insertString(int offset, String str, AttributeMap set); 
    public void remove(int offset, int length);
    public Position createPosition(int offset);
}
