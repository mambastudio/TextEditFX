/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texteditfx.text;

/**
 *
 * @author jmburu
 */
public class Segment {
    public char[] array;
    public int count;
    public int offset;
    
    public Segment()
    {
        array = null;
        count = 0;
        offset = 0;
    }
    
    public Segment(char[] array)
    {
        if(array == null)
        {
            this.array = null;
            this.count = 0;
            this.offset = 0;
        }
        else
        {
            this.offset = 0;
            this.count = array.length;
            this.array = array;
        }
    }
    
    public Segment(char[] array, int offset, int count)
    {
        if(array == null)
        {
            this.array = null;
            this.count = 0;
            this.offset = 0;
        }
        else
        {
            this.offset = offset;
            this.count = count;
            this.array = array;            
        }
    }
    
    public char charAt(int index)
    {
        return array[index];
    }

    public Segment copy()
    {
        return new Segment(array, offset, count);
    }

    public char current()
    {
        return array[getIndex()];
    }

    public char first()
    {
        offset = getBeginIndex();
        return charAt(offset);
    }

    public int getBeginIndex()
    {
        return 0;
    }

    public int getEndIndex()
    {
        return array.length;
    }

    public int getIndex()
    {
        return offset;
    }

    public char last()
    {
        return charAt(getIndex() - 1);
    }

    public int length()
    {
        return array.length;
    }
    
    public char next()
    {
        char c = array[getIndex()];
        offset++;
        return c;
    }
    
    public char previous()
    {
        offset--;
        char c = array[getIndex()];        
        return c;
    }

    public char setIndex(int position)
    {
        offset = position;
        return charAt(position);
    }


    public CharSequence subSequence(int start, int end)
    {
        if(start == end)
            return "";
        else
            return new String(array, start, end - start);
    }
    
    @Override
    public String toString()
    {
        return new String(array);
    }

}
