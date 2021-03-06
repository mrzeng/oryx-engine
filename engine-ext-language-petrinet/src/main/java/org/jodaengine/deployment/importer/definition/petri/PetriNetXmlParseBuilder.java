/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This code is part of the Activiti project under the above license:
 * 
 *                  http://www.activiti.org
 * 
 * We did some modification which are hereby also under the Apache License, Version 2.0.
 */

package org.jodaengine.deployment.importer.definition.petri;

import org.jodaengine.util.xml.XmlParseBuilderImpl;

/**
 * This builder helps to create {@link PetriNetXmlParse} object. Here it is possible to configure the source of the XML
 * file.
 */
public class PetriNetXmlParseBuilder extends XmlParseBuilderImpl {

    protected PetriNetXmlParser petriNetXmlParser;

    /**
     * Creates a ParserBuilder
     * 
     * @param petriNetXmlParser a xml parser instance
     */
    public PetriNetXmlParseBuilder(PetriNetXmlParser petriNetXmlParser) {

        super(petriNetXmlParser);
        this.petriNetXmlParser = petriNetXmlParser;
    }

    @Override
    public PetriNetXmlParse buildXmlParse() {

        return new PetriNetXmlParse(petriNetXmlParser, streamSource);
    }
}
