package com.yonyou.cloud.i18n.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yonyou.iuap.baseservice.attachment.entity.AttachmentEntity;
import com.yonyou.iuap.baseservice.attachment.entity.Attachmentable;
import com.yonyou.iuap.baseservice.entity.AbsDrModel;
import com.yonyou.iuap.baseservice.entity.annotation.CodingEntity;
import com.yonyou.iuap.baseservice.support.condition.Condition;
import com.yonyou.iuap.baseservice.support.generator.GeneratedValue;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * 国际化工具
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "i18n")

@CodingEntity(codingField = "")
public class I18n extends AbsDrModel implements Serializable, Attachmentable {
    @Id
    @GeneratedValue
    @Condition
    protected String id;//ID

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(Serializable id) {
        this.id = id.toString();
        super.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Condition
    @Column(name = "NAME")
    private String name;        //名称

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {

        return this.name;
    }

    @Condition
    @Column(name = "ATTACH_ID")
    private String attachId;        //附件关联ID

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }


    @Transient
    private List<AttachmentEntity> attachment;

    public List<AttachmentEntity> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<AttachmentEntity> attachment) {
        this.attachment = attachment;
    }


}




