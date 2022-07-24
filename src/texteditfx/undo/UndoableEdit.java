/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx.undo;

/**
 *
 * @author user
 */
public interface UndoableEdit {
    public boolean  addEdit(UndoableEdit anEdit);
    public boolean  canRedo();
    public boolean  canUndo();
    public void     die();
    public void     redo();
    public boolean  replaceEdit(UndoableEdit anEdit);
    public void     undo();
}
