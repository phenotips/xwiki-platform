/*
 * Copyright 2006-2007, XpertNet SARL, and individual contributors as indicated
 * by the contributors.txt.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.xmlrpc;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.doc.XWikiDocument;

import java.util.Date;
import java.util.Map;

public class Page extends PageSummary
{
    private int version;

    private String content;

    private Date created;

    private String creator;

    private Date modified;

    private String modifier;

    private boolean homepage;

    public Page(String id, String space, String parentId, String title, String url,
        int version, String content, Date created, String creator,
        Date modified, String modifier, boolean homepage, int locks)
    {
        super(id, space, parentId, title, url, locks);
        this.setVersion(version);
        this.setContent(content);
        this.setCreated(created);
        this.setCreator(creator);
        this.setModified(modified);
        this.setModifier(modifier);
        this.setHomepage(homepage);
    }

    public Page(XWikiDocument doc, XWikiContext context)
    {
        super(doc, context);
        this.setVersion(doc.getRCSVersion().getNumbers()[1]);
        this.setContent(doc.getContent());
        this.setCreated(doc.getCreationDate());
        this.setCreator(doc.getAuthor());
        this.setModified(doc.getDate());
        this.setModifier(doc.getAuthor());
        this.setHomepage((doc.getName().equals("WebHome")));
    }

    public Page(Map parameters)
    {
        super(parameters);
        this.setVersion(((Integer) parameters.get("version")).intValue());
        this.setContent((String) parameters.get("content"));
        this.setCreated((Date) parameters.get("created"));
        this.setCreator((String) parameters.get("creator"));
        this.setModified((Date) parameters.get("modified"));
        this.setModifier((String) parameters.get("modifier"));
        this.setHomepage(((Boolean) parameters.get("homepage")).booleanValue());
    }

    Map getParameters()
    {
        Map params = super.getParameters();
        params.put("version", new Integer(getVersion()));
        params.put("content", getContent());
        params.put("created", getCreated());
        params.put("creator", getCreator());
        params.put("modified", getModified());
        params.put("modifier", getModifier());
        params.put("homepage", new Boolean(isHomepage()));
        return params;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public Date getModified()
    {
        return modified;
    }

    public void setModified(Date modified)
    {
        this.modified = modified;
    }

    public String getModifier()
    {
        return modifier;
    }

    public void setModifier(String modifier)
    {
        this.modifier = modifier;
    }

    public boolean isHomepage()
    {
        return homepage;
    }

    public void setHomepage(boolean homepage)
    {
        this.homepage = homepage;
    }
}
