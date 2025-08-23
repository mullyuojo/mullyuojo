package com.ojo.mullyuojo.company.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompanyProduct is a Querydsl query type for CompanyProduct
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyProduct extends EntityPathBase<CompanyProduct> {

    private static final long serialVersionUID = 1296384614L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCompanyProduct companyProduct = new QCompanyProduct("companyProduct");

    public final QCompany company;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QCompanyProduct(String variable) {
        this(CompanyProduct.class, forVariable(variable), INITS);
    }

    public QCompanyProduct(Path<? extends CompanyProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCompanyProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCompanyProduct(PathMetadata metadata, PathInits inits) {
        this(CompanyProduct.class, metadata, inits);
    }

    public QCompanyProduct(Class<? extends CompanyProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new QCompany(forProperty("company")) : null;
    }

}

