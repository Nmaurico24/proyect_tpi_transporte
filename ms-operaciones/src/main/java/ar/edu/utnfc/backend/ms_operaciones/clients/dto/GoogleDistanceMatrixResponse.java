package ar.edu.utnfc.backend.ms_operaciones.clients.dto;

import java.util.List;

public class GoogleDistanceMatrixResponse {

    private List<Row> rows;
    private List<String> destination_addresses;
    private List<String> origin_addresses;

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public void setDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    // ==== nested types ====

    public static class Row {
        private List<Element> elements;

        public List<Element> getElements() {
            return elements;
        }

        public void setElements(List<Element> elements) {
            this.elements = elements;
        }
    }

    public static class Element {
        private ValueText distance;
        private ValueText duration;
        private String status;

        public ValueText getDistance() {
            return distance;
        }

        public void setDistance(ValueText distance) {
            this.distance = distance;
        }

        public ValueText getDuration() {
            return duration;
        }

        public void setDuration(ValueText duration) {
            this.duration = duration;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class ValueText {
        private long value; // en metros o segundos
        private String text; // "10 km", "5 mins"

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
