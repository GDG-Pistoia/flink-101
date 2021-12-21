import "normalize.css/normalize.css";
import './index.css';
import 'bootstrap/dist/css/bootstrap.css';
import React, {Fragment, useEffect, useState} from "react";
import { render } from "react-dom";
import {Card, Button, Container, FormControl, Form} from "react-bootstrap";
import Bearing from "./components/Bearing";
import Row from "react-bootstrap/Row";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import InputGroup from "react-bootstrap/InputGroup";
import Col from "react-bootstrap/Col";
import Table from "react-bootstrap/Table";


const useEventSource = (url) => {
    const [data, updateData] = useState(null);

    useEffect(() => {
        const source = new EventSource(url);

        source.onmessage = function logEvents(event) {
            updateData(JSON.parse(event.data));
        }
    }, [])

    return data;
}

const App = () => {
    const [subscriptions, setSubscriptions] = useState([])

    useEffect(() => {
        const source = new EventSource('http://localhost:8888/subscriptions/stats');

        source.onmessage = function logEvents(event) {
            setSubscriptions([...subscriptions, JSON.parse(event.data)]);
        }
    }, [])

    return (
        <>
            <Navbar bg="white" expand="lg" >
                <Navbar.Brand href="#home">
                    <div className="brand">
                        Flink 101
                    </div>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">

                    </Nav>
                </Navbar.Collapse>
                <Nav>
                </Nav>
            </Navbar>
            <Bearing space={"20px"}/>
            <Container>
                <Row>
                    <Col>
                        <InputGroup className="mb-3">
                            <FormControl
                                aria-label="Recipient's username"
                                aria-describedby="basic-addon2"
                            />
                            <Button variant="outline-secondary" id="button-addon2">
                                Add
                            </Button>
                        </InputGroup>

                    </Col>
                </Row>
                <Row>
                    <Col>
                        <Table striped bordered hover>
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Name</th>
                                <th>Interest</th>
                                <th>Stats</th>
                            </tr>
                            </thead>
                            <tbody>
                            {
                                subscriptions.map((subscription, i) => (
                                    <tr key={i}>
                                        <td>
                                            {
                                                i
                                            }
                                        </td>
                                        <td>
                                            {
                                                subscription['f1'][0]
                                            }
                                        </td>
                                        <td>
                                            {
                                                subscription['f0']
                                            }
                                        </td>
                                        <td>
                                            {
                                                subscription['f2']['count']
                                            }
                                        </td>
                                    </tr>
                                ) )
                            }
                            </tbody>
                        </Table>
                        {
                            subscriptions.length === 0 ?
                                <Fragment>
                                    <Bearing space={50}/>
                                    <Row>
                                        <Col className="text-center">
                                            <strong>Not subscriptions found :(</strong>
                                        </Col>
                                    </Row>
                                </Fragment> : null
                        }
                    </Col>
                </Row>
            </Container>
        </>
    )
};


render(<App/>, window.document.getElementById("app"));
