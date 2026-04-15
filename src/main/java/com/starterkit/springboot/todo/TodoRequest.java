package com.starterkit.springboot.todo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TodoRequest {

    @NotBlank
    @Size(max = 120)
    private String title;

    @Size(max = 1000)
    private String description;

    private Boolean done;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getDone() { return done; }
    public void setDone(Boolean done) { this.done = done; }
}
