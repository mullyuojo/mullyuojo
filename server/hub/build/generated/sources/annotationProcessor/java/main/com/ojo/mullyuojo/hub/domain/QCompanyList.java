package com.ojo.mullyuojo.hub.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompanyList is a Querydsl query type for CompanyList
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyList extends EntityPathBase<CompanyList> {

    private static final long serialVersionUID = -31099505L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyList companyList = new QCompanyList("companyList");

    public final QHub hub;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QCompanyList(String variable) {
        this(CompanyList.class, forVariable(variable), INITS);
    }

    public QCompanyList(Path<? extends CompanyList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCompanyList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCompanyList(PathMetadata metadata, PathInits inits) {
        this(CompanyList.class, metadata, inits);
    }

    public QCompanyList(Class<? extends CompanyList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hub = inits.isInitialized("hub") ? new QHub(forProperty("hub")) : null;
    }

}

