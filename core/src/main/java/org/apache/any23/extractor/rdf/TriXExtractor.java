/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.any23.extractor.rdf;

import org.apache.any23.extractor.ExtractionContext;
import org.apache.any23.extractor.ExtractionResult;
import org.apache.any23.extractor.ExtractorDescription;
import org.eclipse.rdf4j.rio.RDFParser;

/**
 * Concrete implementation of {@link org.apache.any23.extractor.Extractor.ContentExtractor}
 * to perform extraction on <a href="http://www.w3.org/2004/03/trix/">TriX</a> documents.
 *
 * @author Michele Mostarda (mostarda@fbk.eu)
 */
public class TriXExtractor extends BaseRDFExtractor {

    /**
     * Constructor, allows to specify the validation and error handling policies.
     *
     * @param verifyDataType   if <code>true</code> the data types will be verified,
     *                         if <code>false</code> will be ignored.
     * @param stopAtFirstError if <code>true</code> the parser will stop at first parsing error,
     *                         if <code>false</code> will ignore non blocking errors.
     */
    public TriXExtractor(boolean verifyDataType, boolean stopAtFirstError) {
        super(verifyDataType, stopAtFirstError);
    }

    /**
     * Default constructor, with no verification of data types and not stop at first error.
     */
    public TriXExtractor() {
        this(true, true);
    }

    @Override
    public ExtractorDescription getDescription() {
        return TriXExtractorFactory.getDescriptionInstance();
    }

    @Override
    protected RDFParser getParser(ExtractionContext extractionContext, ExtractionResult extractionResult) {
        return RDFParserFactory.getInstance().getTriXParser(
                isVerifyDataType(), isStopAtFirstError(), extractionContext, extractionResult
        );
    }


}
