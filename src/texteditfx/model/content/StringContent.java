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
public class StringContent implements Content {
    StringBuilder content = new StringBuilder();
    
    @Override
    public Position createPosition(int offset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int length() {
        return content.length();
    }

    @Override
    public UndoableEdit insertString(int where, String str) {
        content.insert(where, str);
        return null;
    }

    @Override
    public UndoableEdit remove(int where, int nitems) {
        content.replace(where, where + nitems, "");
        return null;
    }

    @Override
    public String getString(int where, int len) {
        return content.subSequence(where, where + len).toString();
        
    }

    @Override
    public void getChars(int where, int len, Segment txt) {
        String s = content.substring(where, where + len);
        txt.array = s.toCharArray();
        txt.offset = where;
        txt.count = len;
    }
    
}
