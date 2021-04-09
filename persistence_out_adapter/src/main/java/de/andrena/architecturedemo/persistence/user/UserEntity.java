package de.andrena.architecturedemo.persistence.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import de.andrena.architecturedemo.domain.user.User;
import de.andrena.architecturedemo.persistence.role.RoleEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@SequenceGenerator(name = "user_id_generator", sequenceName = "user_sequence", allocationSize = 1)
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
	private Long id;
	@Version
	private Long version;

	@Column(name = "name", unique = true)
	@NotNull
	@NotEmpty
	private String name;
	@Column(unique = true)
	private String displayName;

	@Column(unique = true)
	private String domainId;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "roles_id"))
	private List<RoleEntity> roles = new ArrayList<>();

	public User toBusinessObject() {
		return new User(version, domainId, name, displayName, roles.stream().map(RoleEntity::toBusinessObject).collect(Collectors.toList()));
	}

	public static UserEntity fromBusinessObject(User user) {
		UserEntity userEntity = new UserEntity();

		userEntity.setDomainId(user.getDomainId());
		userEntity.setVersion(user.getVersion());
		userEntity.setName(user.getLoginId());
		userEntity.setRoles(user.getRoles().stream().map(role -> RoleEntity.fromBusinessObject(role)).collect(Collectors.toList()));
		userEntity.setDisplayName(user.getDisplayName());
		return userEntity;
	}

	public UserEntity update(User user) {
		setName(user.getLoginId());
		setDisplayName(user.getDisplayName());
		return this;
	}

}
