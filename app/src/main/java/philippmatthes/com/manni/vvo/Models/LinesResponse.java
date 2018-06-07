package main.Models;

import java.util.Date;
import java.util.List;

public class LinesResponse {
    private List<Line> lines;
    private Date expirationTime;

    public LinesResponse(List<Line> lines, Date expirationTime) {
        this.lines = lines;
        this.expirationTime = expirationTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public List<Line> getLines() {
        return lines;
    }
}
