package co.kr.cocomu.study.domain;


import co.kr.cocomu.admin.dto.response.JudgeResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cocomu_judge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Judge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "judge_id")
    private Long id;
    private String name;
    private String imageUrl;

    private Judge(final String name, final String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static Judge of(final String name, final String imageUrl) {
        return new Judge(name, imageUrl);
    }

    public JudgeResponse toDto() {
        return new JudgeResponse(this.id, this.name, this.imageUrl);
    }

}
