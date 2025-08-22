package com.ojo.mullyuojo.hub.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeliveryManagerList is a Querydsl query type for DeliveryManagerList
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeliveryManagerList extends EntityPathBase<DeliveryManagerList> {

    private static final long serialVersionUID = 1899194411L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeliveryManagerList deliveryManagerList = new QDeliveryManagerList("deliveryManagerList");

    public final QHub hub;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QDeliveryManagerList(String variable) {
        this(DeliveryManagerList.class, forVariable(variable), INITS);
    }

    public QDeliveryManagerList(Path<? extends DeliveryManagerList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeliveryManagerList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeliveryManagerList(PathMetadata metadata, PathInits inits) {
        this(DeliveryManagerList.class, metadata, inits);
    }

    public QDeliveryManagerList(Class<? extends DeliveryManagerList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hub = inits.isInitialized("hub") ? new QHub(forProperty("hub")) : null;
    }

}

