package cz.cvut.fel.ear.tabletopreservations.rest.reponses;

import cz.cvut.fel.ear.tabletopreservations.util.RestUtils;

public class UpdatedResponse {
    private Integer id;
    private String url;

    public UpdatedResponse(Integer id) {
        this.id = id;
        this.url = RestUtils.createResourceUrl(id);
    }

    public UpdatedResponse(Integer id, String basePath) {
        this.id = id;
        this.url = RestUtils.createResourceUrl(basePath, id);;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

