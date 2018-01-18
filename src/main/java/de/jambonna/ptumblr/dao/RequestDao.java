package de.jambonna.ptumblr.dao;

import de.jambonna.ptumblr.domain.Request;


/**
 * Persistence operations relative to Request entities
 */
public interface RequestDao {
    public Request findLastRequest();
    public void saveRequest(Request r);
}
