/**
 * ===================================================================
 *
 * Copyright (c) 2003 Ludovic Dubost, All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details, published at 
 * http://www.gnu.org/copyleft/lesser.html or in lesser.txt in the
 * root folder of this distribution.

 * Created by
 * User: Ludovic Dubost
 * Date: 21 janv. 2004
 * Time: 11:26:09
 */
package com.xpn.xwiki.plugin;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWiki;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.util.Util;
import com.xpn.xwiki.render.HeadingSubstitution;
import com.xpn.xwiki.render.XWikiWikiBaseRenderer;
import com.xpn.xwiki.render.WikiSubstitution;
import com.xpn.xwiki.doc.XWikiDocInterface;

import java.util.Vector;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Pattern;
import org.apache.tools.ant.util.StringUtils;
import org.apache.ecs.filter.CharacterFilter;
import org.apache.ecs.Entities;

public class PatternPlugin extends XWikiDefaultPlugin {
    Vector patterns = new Vector();
    Vector results = new Vector();
    Vector descriptions = new Vector();
    WikiSubstitution patternListSubstitution;

    public PatternPlugin(String name, XWikiContext context) {
        super(name, context);
        init(context);

        // TODO: register for any modifications of the Plugins.PatternPlugin document..
        // TODO: the notification method needs to be implemented.
    }

    public void init(XWikiContext context) {
        try {
            XWiki xwiki = context.getWiki();
            XWikiDocInterface pattern_doc = xwiki.getDocument("Plugins", "PatternPlugin");
            String content = pattern_doc.getContent();

            Vector patternlist = pattern_doc.getObjects("Plugins.PatternPlugin");
            if (patternlist!=null) {
            for (int i=0;i<patternlist.size();i++) {
                    BaseObject obj = (BaseObject) patternlist.get(i);
                    patterns.add(obj.get("pattern").toString());
                    results.add(obj.get("result").toString());
                    descriptions.add(obj.get("description").toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        patternListSubstitution = new WikiSubstitution(context.getUtil(),"%PATTERNS%");
    }


    public void addPattern(String pattern, String result, String description) {
        patterns.add(pattern);
        results.add(result);
        descriptions.add(description);
    }

    public String getPatternList() {
        CharacterFilter filter = new CharacterFilter();
        StringBuffer list = new StringBuffer();
        list.append("<pre>\n");
        list.append("<table border=1>");
        list.append("<tr><td><strong>Pattern</strong></td>");
        list.append("<td><strong>Result</strong></td><td><strong>Description</strong></td></tr>");
        for (int i=0;i<patterns.size();i++) {
            list.append("<tr><td><nop>");
            list.append(filter.process((String)patterns.get(i)));
            list.append("</td><td>");
            list.append(filter.process((String)results.get(i)));
            list.append("</td><td>");
            list.append(descriptions.get(i));
            list.append("</td></tr>");
        }
        list.append("</table>");
        list.append("\n<pre>");
        return list.toString();
    }

    public String commonTagsHandler(String line, XWikiContext context) {
        String subst = getPatternList();
        subst = StringUtils.replace(subst,"$","\\$");
        patternListSubstitution.setSubstitution(subst);
        line = patternListSubstitution.substitute(line);
        return line;
    }

    public String startRenderingHandler(String line, XWikiContext context) {
        return line;
    }

    public String outsidePREHandler(String line, XWikiContext context) {
        Util util = context.getUtil();

        for (int i=0;i<patterns.size();i++) {
         String pattern = (String) patterns.get(i);
         String result = (String) results.get(i);
         try {
           if (pattern.startsWith("s/"))
            line = util.substitute(pattern, line);
           else
            line = StringUtils.replace(line, pattern, result);
         } catch (Exception e) {
             // Log a error but do not fail..
         }
        }

        return line;
    }

    public String insidePREHandler(String line, XWikiContext context) {
        return line;
    }

    public String endRenderingHandler(String line, XWikiContext context) {
        return line;
    }


}
