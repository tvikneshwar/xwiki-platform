/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
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
package com.xpn.xwiki.tool.backup;

import java.io.File;
import java.io.IOException;

import org.xwiki.tool.utils.OldCoreHelper;

import com.xpn.xwiki.plugin.packaging.Package;
import com.xpn.xwiki.plugin.packaging.PackageException;

/**
 * Export a set of XWiki documents from an existing database into the file system.
 *
 * @version $Id$
 */
public class Exporter
{
    private OldCoreHelper oldCoreHelper;

    /**
     * @param oldCoreHelper various tools to manipulate oldcore APIs
     */
    public Exporter(OldCoreHelper oldCoreHelper)
    {
        this.oldCoreHelper = oldCoreHelper;
    }

    /**
     * Export documents from an existing loaded XWiki database. The database is defined by its passed name and by an
     * Hibernate configuration file.
     *
     * @param exportDirectory the directory where to export the documents
     * @param databaseName some database name (TODO: find out what this name is really)
     * @throws Exception if the export failed for any reason
     */
    // TODO: Replace the Hibernate config file with a list of parameters required for the exportation
    public void exportDocuments(File exportDirectory, String databaseName) throws Exception
    {
        Package pack = new Package();
        pack.setWithVersions(false);
        pack.addAllWikiDocuments(this.oldCoreHelper.getXWikiContext());

        // TODO: The readFromDir method should not throw IOExceptions, only PackageException.
        // See https://jira.xwiki.org/browse/XWIKI-458
        try {
            pack.exportToDir(exportDirectory, this.oldCoreHelper.getXWikiContext());
        } catch (IOException e) {
            throw new PackageException(PackageException.ERROR_PACKAGE_UNKNOWN,
                "Failed to export documents to [" + exportDirectory + "]", e);
        }
    }
}
