/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

// Server-side JavaScript for the head.html logic
use(["/libs/wcm/foundation/components/utils/AuthoringUtils.js"], function () {

    var StringUtils = Packages.org.apache.commons.lang3.StringUtils;
    var URI = Packages.java.net.URI;

    try {
        var uri = new URI(this.link);

        var path = uri.getPath();
        var fragment = uri.getFragment();
        var scheme = uri.getScheme();

        //remove jcr content from path
        if (StringUtils.endsWith(path, "/jcr:content")) {
            path = path.substring(0, path.length() - 11);
        }

        //if the existing path is relative, and has no extension, add .html
        if (scheme==null && StringUtils.startsWith(path, "/content") && path.indexOf(".")  == -1) {
            path += ".html";
        }

        //build and return the new URI
        var newUri = new URI(uri.getScheme(), uri.getAuthority(), path, uri.getQuery(), fragment);

        return {
            formattedLink : newUri.toString()
        };
    } catch(err) {
        console.log("link:" + this.link + ".\n" + err)
        return {
            formattedLink : this.link
        };
    }
});
