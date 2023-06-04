import AuthButton from "@/components/AuthButton";
import AuthInput from "@/components/AuthInput.";
import Head from "next/head";
import { useState } from "react";

export default function LogIn() {
  const [email, setEmail] = useState("");

  const [password, setPassword] = useState("");

  return (
    <>
      <Head>
        <title>Login | Yeah Board</title>
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
          value={password}
          setValue={setPassword}
          type="password"
          placeholder="Password"
        />

        <AuthButton
          value="로그인"
          onClick={() => {
            console.log(email, password);
          }}
        />
      </div>
    </>
  );
}
