package com.github.norwick.reciperodeo.domain;

//import java.util.UUID;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.validation.constraints.NotNull;
//
//import org.hibernate.annotations.ColumnTransformer;
//import org.hibernate.annotations.GenericGenerator;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;

/**
 * Entity representing a password, so that password isn't fetched each time user is fetched
 * @author Norwick Lee
 */
//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
public class Password {
	
//	@Id
//	@GeneratedValue(generator = "UUID")
//	@GenericGenerator(
//        name = "UUID",
//        strategy = "org.hibernate.id.UUIDGenerator"
//    )
//    @Column(columnDefinition = "BINARY(16)")
//	@ColumnTransformer(write="uuid_to_bin(?)")
//	private UUID id;
//	
//	@NotNull
//	private String hash;
//	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null) return false;
//		if (!(obj instanceof Password)) return false;
//		Password p = (Password) obj;
//		return (this.id.equals(p.id));
//	}
//	
//	@Override
//	public int hashCode() {
//		return id.hashCode();
//	}
}
