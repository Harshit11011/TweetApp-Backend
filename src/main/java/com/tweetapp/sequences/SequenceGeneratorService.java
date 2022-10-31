package com.tweetapp.sequences;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class SequenceGeneratorService {
	
	@Autowired 
	private MongoOperations mongoOperations;
	
	public long generateSequence(String seqName) {
		//get the sequence number
		Query query=new Query(Criteria.where("_id").is(seqName));
		//update the sequence
		Update update =new Update().inc("sequence",1);
		//modify the document
	    DatabaseSequence counter = mongoOperations.findAndModify(query,update,options().returnNew(true).upsert(true),DatabaseSequence.class);
	    return !Objects.isNull(counter) ? counter.getSequence() : 1;
	}

}
