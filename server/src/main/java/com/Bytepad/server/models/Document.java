package com.Bytepad.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Document {

    public enum PaperType{
        QUES("Question Paper"), SOL("Answer Sheets");

        private String paperType;

        PaperType(String paperType){
            this.paperType = paperType;
        }

    }

    public enum SemesterType{
        ODD, EVEN;
    }


    @Id
    @Column(nullable = false, unique = true)
    private UUID documentId;
    @Column(nullable = false)
    private String subjectName;
    @Column(nullable = false)
    private PaperType paperType;
    @Column(nullable = false)
    private SemesterType semesterType;
    @Column(nullable = false)
    private String documentURI;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private CourseBranch courseBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Session session;


    protected Document() {
    }

    public Document(String subjectName, PaperType type, SemesterType semesterType, String documentURI) {
        this.documentId = UUID.randomUUID();
        this.subjectName = subjectName;
        this.paperType = type;
        this.semesterType = semesterType;
        this.documentURI = documentURI;
    }

    public String getPaperType(){
        return this.paperType.paperType;
    }

}
