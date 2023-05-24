package ru.netology.filestorage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditNameRequest {
   // @JsonProperty("filename")
    private String newFilename;
}
