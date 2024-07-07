package com.knu.linkmoa.domain.site.entity;


import com.knu.linkmoa.domain.directory.entity.Directory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="sites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Site {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="site_id")
    private Long id;

    @Column(name="site_name")
    private String siteName;

    @ManyToOne(fetch=FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="directory_id")
    private Directory directory;


    public void setDirectory(Directory directory){
        this.directory = directory;
        directory.getSites().add(this);
    }
}
