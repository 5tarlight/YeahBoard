import AuthButton from "@/components/AuthButton";
import AuthInput from "@/components/AuthInput.";
import Head from "next/head";
import { useState } from "react";

export default function SignUp() {
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  return (
    <>
      <Head>
        <title>Sign Up | Yeah Board</title>
      </Head>

      <div className="auth-container">
        <h2 className="auth-title">회원가입</h2>

        <AuthInput
          value={email}
          setValue={setEmail}
          type="email"
          placeholder="Email"
        />
        <AuthInput
          value={username}
          setValue={setUsername}
          type="text"
          placeholder="Username"
        />
        <AuthInput
          value={password}
          setValue={setPassword}
          type="password"
          placeholder="Password"
        />

        <AuthButton
          value="회원가입"
          onClick={() => {
            console.log(email, username, password);
          }}
        />
      </div>
    </>
  );
}
