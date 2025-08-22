package com.ojo.mullyuojo.company.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCompany is a Querydsl query type for Company
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = -689492407L;

    public static final QCompany company = new QCompany("company");

    public final StringPath address = createString("address");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath deletedBy = createString("deletedBy");

    public final NumberPath<Long> hubId = createNumber("hubId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<CompanyManager, QCompanyManager> managers = this.<CompanyManager, QCompanyManager>createList("managers", CompanyManager.class, QCompanyManager.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final ListPath<CompanyProduct, QCompanyProduct> products = this.<CompanyProduct, QCompanyProduct>createList("products", CompanyProduct.class, QCompanyProduct.class, PathInits.DIRECT2);

    public final EnumPath<CompanyType> type = createEnum("type", CompanyType.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public final StringPath writer = createString("writer");

    public QCompany(String variable) {
        super(Company.class, forVariable(variable));
    }

    public QCompany(Path<? extends Company> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompany(PathMetadata metadata) {
        super(Company.class, metadata);
    }

}

