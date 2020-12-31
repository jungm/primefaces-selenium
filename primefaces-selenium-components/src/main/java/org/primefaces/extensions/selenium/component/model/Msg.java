/*
 * Copyright 2011-2021 PrimeFaces Extensions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.extensions.selenium.component.model;

import java.util.Objects;
import java.util.StringJoiner;

public class Msg {

    private Severity severity;
    private String summary;
    private String detail;

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Msg.class.getSimpleName() + "[", "]")
                    .add("severity=" + severity)
                    .add("summary='" + summary + "'")
                    .add("detail='" + detail + "'")
                    .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Msg)) {
            return false;
        }
        Msg msg = (Msg) o;
        return getSeverity() == msg.getSeverity() &&
                    Objects.equals(getSummary(), msg.getSummary()) &&
                    Objects.equals(getDetail(), msg.getDetail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeverity(), getSummary(), getDetail());
    }
}
