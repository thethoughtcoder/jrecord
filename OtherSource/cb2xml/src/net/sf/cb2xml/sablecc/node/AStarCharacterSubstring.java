/* This file was generated by SableCC (http://www.sablecc.org/). */

package net.sf.cb2xml.sablecc.node;

import java.util.*;
import net.sf.cb2xml.sablecc.analysis.*;

public final class AStarCharacterSubstring extends PCharacterSubstring
{
    private TStar _star_;

    public AStarCharacterSubstring()
    {
    }

    public AStarCharacterSubstring(
        TStar _star_)
    {
        setStar(_star_);

    }
    public Object clone()
    {
        return new AStarCharacterSubstring(
            (TStar) cloneNode(_star_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAStarCharacterSubstring(this);
    }

    public TStar getStar()
    {
        return _star_;
    }

    public void setStar(TStar node)
    {
        if(_star_ != null)
        {
            _star_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _star_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_star_);
    }

    void removeChild(Node child)
    {
        if(_star_ == child)
        {
            _star_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_star_ == oldChild)
        {
            setStar((TStar) newChild);
            return;
        }

    }
}
