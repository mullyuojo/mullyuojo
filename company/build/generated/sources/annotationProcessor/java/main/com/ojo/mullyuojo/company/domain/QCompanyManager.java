package com.ojo.mullyuojo.company.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompanyManager is a Querydsl query type for CompanyManager
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyManager extends EntityPathBase<CompanyManager> {

    private static final long serialVersionUID = -1853848284L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyManager companyManager = new QCompanyManager("companyManager");

    public final QCompany company;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath slackId = createString("slackId");

    public QCompanyManager(String variable) {
        this(CompanyManager.class, forVariable(variable), INITS);
    }

    public QCompanyManager(Path<? extends CompanyManager> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCompanyManager(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCompanyManager(PathMetadata metadata, PathInits inits) {
        this(CompanyManager.class, metadata, inits);
    }

    public QCompanyManager(Class<? extends CompanyManager> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
    }

}

