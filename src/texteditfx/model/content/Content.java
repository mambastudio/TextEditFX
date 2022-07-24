/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx.model.content;

import texteditfx.text.Position;
import texteditfx.text.Segment;
import texteditfx.undo.UndoableEdit;

/**
 *
 * @author user
 */
public interface Content {

    public Position createPosition(int offset);    
    public int length();
    public UndoableEdit insertString(int where, String str);
    public UndoableEdit remove(int where, int nitems);
    public String getString(int where, int len);
    public void getChars(int where, int len, Segment txt);
}
