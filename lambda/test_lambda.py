import pytest
import json
from unittest.mock import patch, MagicMock
from lambda_function import generate_presigned_url

# Test-data, not real name, key or url
BUCKET_NAME = "test-bucket"
PDF_KEY = "test.pdf"
PRESIGNED_URL = "https://s3.amazonaws.com/test-bucket/test.pdf?AWSAccessKeyId=test&Expires=1234567890"

# Mock of the AWS client and session to interact with S3
@pytest.fixture
def mock_s3():
    mock_client = MagicMock()
    mock_session = MagicMock()
    with patch("lambda_function.boto3.Session", return_value=mock_session):
        mock_session.client.return_value = mock_client
        mock_client.generate_presigned_url.return_value = PRESIGNED_URL
        yield mock_session

# Mock of the logger
@pytest.fixture
def mock_logger():
    with patch("lambda_function.logger") as mock_log:
        yield mock_log


# Test for the generate_presigned_url function happy scenario
def test_generate_presigned_url_success(mock_s3, mock_logger):
    # Get presigned url based on pdf key
    response = generate_presigned_url(PDF_KEY, session=mock_s3)
    
    # Check content of the response
    assert response["statusCode"] == 200
    assert "url" in json.loads(response["body"])
    assert json.loads(response["body"])["url"] == PRESIGNED_URL

    # Check if the logger was called as expected
    mock_logger.info.assert_called_with("Ticket generation successful! Returning presigned URL...")


# Test for the generate_presigned_url function failure scenario
def test_generate_presigned_url_failure(mock_s3, mock_logger):
    mock_s3.client.return_value.generate_presigned_url.side_effect = Exception("S3 error")
    
    # Get presigned url based on pdf key, this time it should fail
    response = generate_presigned_url(PDF_KEY, session=mock_s3)
    print(response)
    
    # Check content of the response
    assert response["statusCode"] == 500
    body = json.loads(response["body"])
    assert "error" in body
    assert body["error"] == "Failed to generate URL"
    assert "details" in body

    # Check that the logger logged an error
    mock_logger.error.assert_called_with("Failed to generate presigned URL: S3 error")