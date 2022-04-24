package com.weshare.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weshare.common.AddGroup;
import com.weshare.common.UpdateGroup;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	@Null(groups = AddGroup.class)
	private Long id;

	@Column(name = "belong_user_id")
	@NotNull(groups = AddGroup.class)
	private Long belongUserId;  // 冗余字段：该推文所属者user_id

	@Column(name = "post_id")
	@NotNull(groups = {AddGroup.class, UpdateGroup.class})
	private Long postId;

	@Column(name="user_id")
	@NotNull(groups = AddGroup.class)
	private Long userId;

	@Column(name="content")
	@NotNull(groups = AddGroup.class, message = "评论内容不可以为空")
	private String content;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;
}
