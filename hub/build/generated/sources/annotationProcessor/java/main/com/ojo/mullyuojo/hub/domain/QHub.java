package com.ojo.mullyuojo.hub.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHub is a Querydsl query type for Hub
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHub extends EntityPathBase<Hub> {

    private static final long serialVersionUID = -1470479415L;

    public static final QHub hub = new QHub("hub");

    public final StringPath address = createString("address");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath deletedBy = createString("deletedBy");

    public final StringPath hubName = createString("hubName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> latitude = createNumber("latitude", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> longitude = createNumber("longitude", java.math.BigDecimal.class);

    public final StringPath province = createString("province");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public final StringPath writer = createString("writer");

    public QHub(String variable) {
        super(Hub.class, forVariable(variable));
    }

    public QHub(Path<? extends Hub> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHub(PathMetadata metadata) {
        super(Hub.class, metadata);
    }

}

