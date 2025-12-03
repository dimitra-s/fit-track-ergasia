package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.model.ProgressRecord;
import gr.hua.dit.fittrack.core.repository.ProgressRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;

    public ProgressService(final ProgressRepository progressRepository) {

        //checking if the repository is null
        if (progressRepository == null) throw new NullPointerException();
        this.progressRepository = progressRepository;
    }

    public ProgressRecord addProgress(Long userid){
        ProgressRecord progressRecord = new ProgressRecord();
        progressRecord.setId(userid);
        progressRecord.setDate(progressRecord.getDate());
        progressRecord.setNotes(progressRecord.getNotes());
        progressRecord.setWeight(progressRecord.getWeight());
        return progressRepository.save(progressRecord);
    }

    public Optional<ProgressRecord> listProgress(ProgressRecord progressRecord){
        return progressRepository.findById(progressRecord.getId());
    }
}
