package de.jambonna.ptumblr.dao;

import de.jambonna.ptumblr.domain.Request;


/**
 *
 */
public interface RequestDao {
    public Request findLastRequest();
    public void saveRequest(Request r);
}
