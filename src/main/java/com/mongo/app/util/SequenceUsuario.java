package com.mongo.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Objects;

import com.mongo.app.models.DatabaSequence;

@Component
public class SequenceUsuario {
	@Autowired
	private MongoOperations mongo;
	
	public long generateSequence(String seqName) {
		DatabaSequence counter=mongo.findAndModify(query(where("_id")),
				new Update().inc("seq",1), options().returnNew(true).upsert(true),
				DatabaSequence.class);
		return !Objects.isNull(counter) ? counter.getSeq() : 1; 
	}
}
