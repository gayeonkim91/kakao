DROP TABLE IF EXISTS distribution_requests;

CREATE TABLE distribution_requests (
    distributionRequestId BIGINT NOT NULL AUTO_INCREMENT,
    requestedUserId INT NOT NULL,
    roomId VARCHAR(20) NOT NULL,
    token VARCHAR(3) NOT NULL,
    amount decimal(12,2) NOT NULL,
    memberCount INT NOT NULL,
    createdAt datetime NOT NULL,
    PRIMARY KEY (distributionRequestId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX distribution_request_idx01 ON distribution_requests (roomId, token);

DROP TABLE IF EXISTS distribution_results;

CREATE TABLE distribution_results (
    distributionResultId BIGINT NOT NULL AUTO_INCREMENT,
    receivedUserId INT NOT NULL,
    amount decimal(12, 2) NOT NULL,
    distributionRequestId BIGINT NOT NULL,
    createdAt datetime NOT NULL,
    PRIMARY KEY (distributionResultId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX distribution_result_idx01 ON distribution_results (distributionRequestId)